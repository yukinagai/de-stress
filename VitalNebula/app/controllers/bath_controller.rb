class BathController < ApplicationController
  def report
    if !params[:sensor] || !params[:value]
      render :text => "error"
      return
    end

    Bath.create(
      :sensor => params[:sensor],
      :value  => params[:value]
    )

    render :json => params
  end
end
