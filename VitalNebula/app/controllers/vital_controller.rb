class VitalController < ApplicationController
  def index
  end

  def report
    if !params[:device] || !params[:sensor] || !params[:value]
      render :text => "error"
      return
    end

    Vital.create(
      :device => params[:device],
      :sensor => params[:sensor],
      :value  => params[:value]
    )

    render :json => params
  end
end
